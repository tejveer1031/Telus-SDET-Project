# Use verified Maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Clone repository directly from GitHub
ARG GITHUB_REPO=https://github.com/tejveer1031/Telus-SDET-Project
ARG BRANCH=main
RUN git clone --branch ${BRANCH} ${GITHUB_REPO} /app

# Install browsers and dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
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
    bzip2 \
    # Install Chrome
    && wget -nv -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y /tmp/chrome.deb \
    && rm /tmp/chrome.deb \
    # Install Firefox ESR (fixed URL)
    && wget -nv -O /tmp/firefox.tar.bz2 "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" \
    && tar -xjf /tmp/firefox.tar.bz2 -C /opt \
    && ln -s /opt/firefox/firefox /usr/local/bin/firefox \
    && rm /tmp/firefox.tar.bz2 \
    # Cleanup
    && apt-get purge -y --auto-remove bzip2 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Resolve dependencies (cached in image)
RUN mvn dependency:resolve

# Command to run tests with Allure reports
CMD xvfb-run --auto-servernum mvn clean verify -DsuiteXmlFile=testng.xml allure:report