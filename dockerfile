# Use official Maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy only essential build files first
COPY pom.xml .
COPY src ./src
COPY testng.xml .

# Install browsers and dependencies with Chrome repository setup
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    wget \
    gnupg \
    && rm -rf /var/lib/apt/lists/* && \
    # Add Chrome repository
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/googlechrome-linux-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/googlechrome-linux-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    google-chrome-stable \
    firefox \
    xvfb \
    libgtk-3-0 \
    libdbus-glib-1-2 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxtst6 \
    libnss3 \
    libcups2 \
    libxss1 \
    libxrandr2 \
    libasound2 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libpangocairo-1.0-0 \
    libgbm1 \
    dbus \
    && rm -rf /var/lib/apt/lists/*

# Resolve dependencies (layer caching optimization)
RUN mvn dependency:resolve

# Copy remaining source code
COPY . .

# Install Allure and configure test execution
RUN mkdir -p target/allure-results && \
    # Install Allure CLI
    curl -o allure-2.24.1.tgz -Ls https://github.com/allure-framework/allure2/releases/download/2.24.1/allure-2.24.1.tgz && \
    tar -zxvf allure-2.24.1.tgz -C /opt/ && \
    ln -s /opt/allure-2.24.1/bin/allure /usr/bin/allure && \
    rm allure-2.24.1.tgz

# Test execution with Xvfb and Allure reporting
CMD sh -c "xvfb-run --auto-servernum mvn clean verify -DsuiteXmlFile=testng.xml && allure generate target/allure-results -o target/allure-report --clean"