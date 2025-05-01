# Use official Maven image with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy only essential build files first
COPY pom.xml .
COPY src ./src
COPY testng.xml .

# Install browsers and dependencies with proper Firefox setup
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    wget \
    gnupg \
    software-properties-common && \
    # Add Chrome repository
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/googlechrome-linux-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/googlechrome-linux-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    # Add Firefox repository
    add-apt-repository ppa:mozillateam/ppa -y && \
    echo "Package: firefox*" > /etc/apt/preferences.d/mozilla-firefox && \
    echo "Pin: release o=LP-PPA-mozillateam" >> /etc/apt/preferences.d/mozilla-firefox && \
    echo "Pin-Priority: 1001" >> /etc/apt/preferences.d/mozilla-firefox && \
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
    dbus && \
    # Install geckodriver
    wget https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz && \
    tar -zxvf geckodriver-*.tar.gz && \
    mv geckodriver /usr/local/bin/ && \
    chmod +x /usr/local/bin/geckodriver && \
    rm geckodriver-*.tar.gz && \
    # Cleanup
    rm -rf /var/lib/apt/lists/*


# Resolve dependencies (layer caching optimization)
RUN mvn dependency:resolve

# Copy remaining source code
COPY . .

# Install Allure and configure test execution
RUN mkdir -p target/allure-results && \
    curl -o allure-2.24.1.tgz -Ls https://github.com/allure-framework/allure2/releases/download/2.24.1/allure-2.24.1.tgz && \
    tar -zxvf allure-2.24.1.tgz -C /opt/ && \
    ln -s /opt/allure-2.24.1/bin/allure /usr/bin/allure && \
    rm allure-2.24.1.tgz

# Test execution with Xvfb and Allure reporting
CMD sh -c "Xvfb :99 -screen 0 1920x1080x24 & \
           export DISPLAY=:99 && \
           mvn clean verify -DsuiteXmlFile=testng.xml \
           -Dwebdriver.gecko.driver=/usr/local/bin/geckodriver \
           -Dselenium.browser=firefox && \
           allure generate target/allure-results -o target/allure-report --clean"