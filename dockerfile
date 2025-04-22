# 1) Builder with JDK & Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

ARG GITHUB_REPO=https://github.com/tejveer1031/Telus-SDET-Project
ARG BRANCH=main
RUN git clone --branch ${BRANCH} ${GITHUB_REPO} /app

WORKDIR /app

# 2) Install browsers & deps
RUN apt-get update && apt-get install -y --no-install-recommends \
      xvfb curl bzip2 libgtk-3-0 libdbus-glib-1-2 \
      libx11-xcb1 libxcomposite1 libxcursor1 libxdamage1 \
      libxi6 libxtst6 libnss3 libcups2 libxss1 libxrandr2 \
      libasound2 libatk1.0-0 libatk-bridge2.0-0 libpangocairo-1.0-0 \
 && rm -rf /var/lib/apt/lists/*

# 3) Chrome via APT repo
RUN apt-get update && apt-get install -y --no-install-recommends \
      wget gnupg ca-certificates \
 && wget -qO- https://dl-ssl.google.com/linux/linux_signing_key.pub \
      | apt-key add - \
 && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
      > /etc/apt/sources.list.d/google-chrome.list \
 && apt-get update && apt-get install -y --no-install-recommends \
      google-chrome-stable \
 && rm -rf /var/lib/apt/lists/*

# 4) Firefox ESR
RUN apt-get update && apt-get install -y --no-install-recommends bzip2 \
 && wget -qO /tmp/firefox-esr.tar.bz2 \
      "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" \
 && tar -xjf /tmp/firefox-esr.tar.bz2 -C /opt \
 && ln -s /opt/firefox/firefox /usr/local/bin/firefox \
 && rm /tmp/firefox-esr.tar.bz2 \
 && apt-get purge -y --auto-remove bzip2 \
 && rm -rf /var/lib/apt/lists/*

# 5) Resolve & build
RUN mvn dependency:resolve

# 6) Copy trigger script
COPY trigger_jenkins.sh /app/trigger_jenkins.sh
RUN chmod +x /app/trigger_jenkins.sh

# 7) Default command
CMD xvfb-run --auto-servernum mvn clean verify \
       -DsuiteXmlFile=testng.xml allure:report \
    && /app/trigger_jenkins.sh
