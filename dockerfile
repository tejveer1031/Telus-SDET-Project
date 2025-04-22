# Use verified Maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Clone repository directly from GitHub
ARG GITHUB_REPO=https://github.com/tejveer1031/Telus-SDET-Project
ARG BRANCH=main
RUN git clone --branch ${BRANCH} ${GITHUB_REPO} /app

# Install browsers, dependencies, and curl
RUN apt-get update && apt-get install -y --no-install-recommends \
    xvfb \
    curl \  # Added curl for Jenkins API calls
    libgtk-3-0 \
    # ... keep existing browser dependencies ...
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Resolve dependencies (cached in image)
RUN mvn dependency:resolve

# Copy trigger script
COPY trigger_jenkins.sh /app/trigger_jenkins.sh
RUN chmod +x /app/trigger_jenkins.sh

# Command to run tests with Allure reports and trigger Jenkins
CMD xvfb-run --auto-servernum mvn clean verify -DsuiteXmlFile=testng.xml allure:report && \
    /app/trigger_jenkins.sh