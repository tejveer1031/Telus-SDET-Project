# Use Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

# Set common environment variables
ENV DISPLAY=:99 \
    TZ=UTC \
    CHROME_VERSION=123.0.6312.122 \
    CHROME_DRIVER_VERSION=123.0.6312.122 \
    GECKO_DRIVER_VERSION=0.34.0

# Install essential dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    unzip \
    xvfb \
    libgconf-2-4 \
    libxss1 \
    libasound2 \
    libgbm1 \
    libpangocairo-1.0-0 \
    fonts-liberation \
    xdg-utils \
    procps \
    gnupg2 \
    software-properties-common \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome
RUN curl -sSL https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    -o /tmp/chrome.deb && \
    apt-get update && \
    apt-get install -y /tmp/chrome.deb && \
    rm -rf /tmp/* /var/lib/apt/lists/*

# Install Firefox from Mozilla PPA (avoid snap)
RUN add-apt-repository -y ppa:mozillateam/ppa && \
    echo 'Package: firefox*' > /etc/apt/preferences.d/mozilla-firefox && \
    echo 'Pin: release o=LP-PPA-mozillateam' >> /etc/apt/preferences.d/mozilla-firefox && \
    echo 'Pin-Priority: 1001' >> /etc/apt/preferences.d/mozilla-firefox && \
    apt-get update && \
    apt-get install -y firefox && \
    rm -rf /var/lib/apt/lists/*

# Install Drivers
RUN mkdir -p /usr/local/bin && \
    # ChromeDriver
    curl -sSL https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/${CHROME_DRIVER_VERSION}/linux64/chromedriver-linux64.zip \
    -o /tmp/chromedriver.zip && \
    unzip -o /tmp/chromedriver.zip -d /tmp/ && \
    mv /tmp/chromedriver-linux64/chromedriver /usr/local/bin/ && \
    # Geckodriver
    curl -sSL https://github.com/mozilla/geckodriver/releases/download/v${GECKO_DRIVER_VERSION}/geckodriver-v${GECKO_DRIVER_VERSION}-linux64.tar.gz \
    -o /tmp/geckodriver.tar.gz && \
    tar -xzf /tmp/geckodriver.tar.gz -C /usr/local/bin/ && \
    # Cleanup and permissions
    rm -rf /tmp/* && \
    chmod 755 /usr/local/bin/chromedriver /usr/local/bin/geckodriver && \
    # Verify installations
    google-chrome --version && \
    chromedriver --version && \
    /usr/lib/firefox/firefox --version && \
    geckodriver --version

# Install Maven
ARG MAVEN_VERSION=3.9.6
RUN mkdir -p /usr/share/maven && \
    curl -fsSL -o /tmp/maven.tar.gz \
    https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf /tmp/maven.tar.gz -C /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Configure workspace
WORKDIR /app
COPY . /app

# Safe line ending conversion
RUN find . -type f -exec file {} \; | grep -E "ASCII|UTF-8|text" | cut -d: -f1 | xargs -d'\n' dos2unix && \
    [ -f "mvnw" ] && chmod +x mvnw || true

# Build application
RUN mvn clean install -DskipTests

RUN apt-get update && apt-get install -y xvfb

# Xvfb configuration with healthcheck
HEALTHCHECK --interval=5s --timeout=5s --retries=5 \
    CMD xdpyinfo -display :99 >/dev/null 2>&1

CMD ["sh", "-c", "Xvfb :99 -screen 0 1920x1080x24 -ac >/dev/null 2>&1 & \
     export DISPLAY=:99 && \
     sleep 2 && \
     mvn test"]