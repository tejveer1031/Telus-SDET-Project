# Use verified Maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Install browsers with proper dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    wget \
    bzip2 \
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
    # Install Chrome
    && wget -nv -O /tmp/chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y /tmp/chrome.deb \
    && rm /tmp/chrome.deb \
    # Install latest Firefox ESR
    && wget -nv -O /tmp/firefox.tar.bz2 "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" \
    && tar -xjf /tmp/firefox.tar.bz2 -C /opt \
    && ln -s /opt/firefox/firefox /usr/local/bin/firefox \
    && rm /tmp/firefox.tar.bz2

# Set display environment for headless execution
ENV DISPLAY=:99
ENV DBUS_SESSION_BUS_ADDRESS=/dev/null

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src/ src/
COPY testng.xml .

# Resolve dependencies (cached in image)
RUN mvn dependency:resolve

# Command to run tests with Allure reports
CMD xvfb-run --auto-servernum mvn test allure:report