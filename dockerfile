# 1) Builder with JDK & Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Use build arguments for workspace setup
ARG WORKSPACE=/app
WORKDIR ${WORKSPACE}

# 2) Copy project files from Jenkins workspace
COPY pom.xml .
COPY src ./src
COPY testng.xml .

# 3) Browser setup - Optimized installation
# Chrome
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget gnupg ca-certificates \
    && wget -qO- https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/googlechrom-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/googlechrom-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y --no-install-recommends \
    google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Firefox ESR
RUN apt-get update && apt-get install -y --no-install-recommends \
    firefox-esr \
    && rm -rf /var/lib/apt/lists/*

# 4) Common dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    xvfb libgtk-3-0 libdbus-glib-1-2 libx11-xcb1 \
    libxcomposite1 libxcursor1 libxdamage1 libxi6 \
    libxtst6 libnss3 libcups2 libxss1 libxrandr2 \
    libasound2 libatk1.0-0 libatk-bridge2.0-0 libpangocairo-1.0-0 \
    && rm -rf /var/lib/apt/lists/*

# 5) Resolve dependencies
RUN mvn dependency:go-offline

# 6) Test execution
CMD xvfb-run --auto-servernum mvn clean verify \
    -DsuiteXmlFile=testng.xml \
    allure:report