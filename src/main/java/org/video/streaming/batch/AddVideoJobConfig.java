package org.video.streaming.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.video.streaming.genre.GenreDto;
import org.video.streaming.person.PersonDto;
import org.video.streaming.video.VideoDto;
import org.video.streaming.video.VideoService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AddVideoJobConfig {

    private final AtomicInteger newVideoCount = new AtomicInteger(0);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private VideoService videoService;
    private String inputVideoFile;
    //
//    @Value("classpath:/Users/bohdan.loiko/IdeaProjects/video-streaming-api/src/main/java/org/video/streaming/batch/videos.csv")
//    private Resource csvFile;

    @Bean
    public Job importUserJob() {
        return new JobBuilder("add-video", jobRepository).incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    public Step step1() {
        return new StepBuilder("step1", jobRepository).<VideoDto, VideoDto>chunk(1, transactionManager)
                                                      .reader(itemReaderCsv())
                                                      .writer(itemWriter())
                                                      .build();
    }

    public ItemReader<VideoDto> itemReader() {
        return () -> {
            VideoDto videoDto = new VideoDto();
            videoDto.setTitle("Title " + UUID.randomUUID());
            videoDto.setDescription("Description " + UUID.randomUUID());
            PersonDto actor = new PersonDto();
            actor.setId(2L);
            videoDto.setActors(List.of(actor));
            PersonDto director = new PersonDto();
            director.setId(1L);
            videoDto.setDirector(director);

            GenreDto genre = new GenreDto();
            genre.setId(1L);
            videoDto.setGenre(genre);
            videoDto.setYearOfRelease(2000);
            videoDto.setViewsCount(100);
            newVideoCount.incrementAndGet();
            if (newVideoCount.get() > 30) {
                return null;
            }
            return videoDto;
        };
    }

    public FlatFileItemReader<VideoDto> itemReaderCsv() {
        InputStream inputStream;
        try {
            //TODO maybe use can:
            // 1. specify this csv
            // 2. upload to the db
            // 3. system will read file from DB

            inputVideoFile =
                    "/Users/bohdan.loiko/IdeaProjects/video-streaming-api/src/main/java/org/video/streaming/batch/videos.csv";
            inputStream = new FileInputStream(inputVideoFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Resource csvFile = new InputStreamResource(inputStream);

        return new FlatFileItemReaderBuilder<VideoDto>().name("videoItemReader")
                                                        .delimited()
                                                        .names("Tittle", "Description", "Director ID", "Actors ID(; separated)", "Genre ID", "Year Of Release")
                                                        .fieldSetMapper(new VideoFieldSetMapper())
                                                        .resource(csvFile)
                                                        .linesToSkip(1)
                                                        .build();
    }

    public ItemWriter<? super VideoDto> itemWriter() {
        return chunk -> chunk.getItems().stream().map(item -> (VideoDto) item).forEach(videoDto -> {
            videoDto = videoService.publishVideo(videoDto);
            videoService.delistVideo(videoDto.getId());
        });
    }
}
