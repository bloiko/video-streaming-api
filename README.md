## Steps to Build and Run the Application
1. Build Your Project:

Ensure your project is built and the JAR file is in the target directory.

###  `mvn clean package`

2. Build Docker Image:

From the root of your project directory, build the Docker image.
```bash
docker build -t your-app-image .
```
3. Start Services with Docker Compose:

Start the application and the MySQL database using Docker Compose.

```bash
docker-compose up
```

4. Use `generated-requests.http` file for HTTP requests