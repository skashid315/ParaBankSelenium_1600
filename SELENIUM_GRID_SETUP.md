# Selenium Grid Setup Guide

This guide explains how to run ParaBank automation tests on **Selenium Grid** for distributed and parallel test execution.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Grid Configuration](#grid-configuration)
- [Setting Up Selenium Grid](#setting-up-selenium-grid)
- [Running Tests on Grid](#running-tests-on-grid)
- [Cloud Grid Providers](#cloud-grid-providers)
- [Troubleshooting](#troubleshooting)

---

## 🔍 Overview

### What is Selenium Grid?
Selenium Grid allows you to:
- Run tests on **multiple machines**
- Execute tests on **different browsers** simultaneously
- Run tests in **parallel** for faster execution
- Scale your test infrastructure

### Grid Architecture
```
┌─────────────────┐         ┌─────────────────┐
│   Your Machine  │────────▶│  Selenium Hub   │
│  (Test Runner)  │         │   (Controller)  │
└─────────────────┘         └────────┬────────┘
                                     │
                    ┌────────────────┼────────────────┐
                    │                │                │
                    ▼                ▼                ▼
            ┌───────────┐    ┌───────────┐    ┌───────────┐
            │ Chrome    │    │ Firefox   │    │  Edge     │
            │  Node     │    │   Node    │    │   Node    │
            └───────────┘    └───────────┘    └───────────┘
```

---

## 🛠️ Prerequisites

- Java JDK 17+
- Maven 3.9+
- Selenium Grid Standalone JAR or Docker
- Network connectivity between machines (for distributed setup)

---

## ⚙️ Grid Configuration

### Step 1: Update config.properties

Edit `src/test/resources/config/config.properties`:

```properties
# ==========================================================
# SELENIUM GRID CONFIGURATION
# ==========================================================

# Enable Grid mode
grid.enabled=true

# Grid Hub URL
# Local Grid: http://localhost:4444/wd/hub
# Docker Grid: http://localhost:4444/wd/hub
# LambdaTest: https://hub.lambdatest.com/wd/hub
# BrowserStack: https://hub-cloud.browserstack.com/wd/hub
grid.url=http://localhost:4444/wd/hub
```

### Step 2: How Framework Detects Grid Mode

| Mode | Configuration |
|------|--------------|
| **Local Mode** | `grid.enabled=false` |
| **Grid Mode** | `grid.enabled=true` |

The `WebDriverFactory` automatically switches between local and remote driver based on this setting.

---

## 🚀 Setting Up Selenium Grid

### Option 1: Local Grid (Single Machine)

#### Step 1: Download Selenium Server
```bash
# Download Selenium Server Standalone JAR
wget https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.28.0/selenium-server-4.28.0.jar
```

#### Step 2: Start Selenium Grid
```bash
# Start Hub and Node together (Standalone mode)
java -jar selenium-server-4.28.0.jar standalone
```

Grid will be available at: `http://localhost:4444`

---

### Option 2: Docker Grid (Recommended)

#### Step 1: Install Docker & Docker Compose
- [Docker Installation Guide](https://docs.docker.com/get-docker/)

#### Step 2: Create docker-compose.yml
```yaml
version: "3"
services:
  chrome:
    image: selenium/node-chrome:latest
    shm_size: 2gb
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SCREEN_WIDTH=1920
      - SCREEN_HEIGHT=1080

  firefox:
    image: selenium/node-firefox:latest
    shm_size: 2gb
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SCREEN_WIDTH=1920
      - SCREEN_HEIGHT=1080

  edge:
    image: selenium/node-edge:latest
    shm_size: 2gb
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
      - SCREEN_WIDTH=1920
      - SCREEN_HEIGHT=1080

  selenium-hub:
    image: selenium/hub:latest
    container_name: selenium-hub
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"
```

#### Step 3: Start Grid
```bash
# Start Selenium Grid with Chrome, Firefox, and Edge
docker-compose up -d

# Verify Grid is running
curl http://localhost:4444/status

# View Grid Console (open in browser)
http://localhost:4444/ui
```

#### Step 4: Stop Grid
```bash
docker-compose down
```

---

### Option 3: Distributed Grid (Multiple Machines)

#### Machine 1: Hub
```bash
# Start Hub only
java -jar selenium-server-4.28.0.jar hub
```

#### Machine 2: Chrome Node
```bash
# Start Chrome Node
java -jar selenium-server-4.28.0.jar node \
  --detect-drivers true \
  --hub http://<HUB_IP>:4444
```

#### Machine 3: Firefox Node
```bash
# Start Firefox Node
java -jar selenium-server-4.28.0.jar node \
  --detect-drivers true \
  --hub http://<HUB_IP>:4444
```

---

## ▶️ Running Tests on Grid

### Step 1: Enable Grid Mode

Update `config.properties`:
```properties
grid.enabled=true
grid.url=http://localhost:4444/wd/hub
```

### Step 2: Run Tests

#### Run All Tests on Grid
```bash
mvn clean test
```

#### Run Specific Tests on Grid
```bash
# Smoke tests on Grid
mvn clean test -Dcucumber.filter.tags="@Smoke"

# Regression tests on Grid
mvn clean test -Dcucumber.filter.tags="@Regression"

# Specific browser on Grid
mvn clean test -Dbrowser=firefox
```

#### Run Tests with Custom Grid URL
```bash
# Override Grid URL via command line
mvn clean test -Dgrid.url=http://192.168.1.100:4444/wd/hub
```

---

## ☁️ Cloud Grid Providers

### LambdaTest

```properties
# config.properties
grid.enabled=true
grid.url=https://<USERNAME>:<ACCESS_KEY>@hub.lambdatest.com/wd/hub
```

```bash
# Run with LambdaTest
mvn clean test -Dgrid.url=https://<USERNAME>:<ACCESS_KEY>@hub.lambdatest.com/wd/hub
```

### BrowserStack

```properties
# config.properties
grid.enabled=true
grid.url=https://<USERNAME>:<ACCESS_KEY>@hub-cloud.browserstack.com/wd/hub
```

```bash
# Run with BrowserStack
mvn clean test -Dgrid.url=https://<USERNAME>:<ACCESS_KEY>@hub-cloud.browserstack.com/wd/hub
```

### Sauce Labs

```properties
# config.properties
grid.enabled=true
grid.url=https://<USERNAME>:<ACCESS_KEY>@ondemand.us-west-1.saucelabs.com:443/wd/hub
```

---

## 🔧 Troubleshooting

### Issue 1: Connection Refused
```
Error: Failed to connect to Selenium Grid
```

**Solution:**
```bash
# Check if Grid is running
curl http://localhost:4444/status

# Verify Grid URL in config.properties
grid.url=http://localhost:4444/wd/hub
```

### Issue 2: Session Not Created
```
Error: Could not start a new session
```

**Solution:**
- Ensure browser nodes are registered with Hub
- Check Grid Console: `http://localhost:4444/ui`
- Verify browser versions match

### Issue 3: Tests Run Locally Instead of Grid

**Solution:**
```bash
# Verify config.properties has
grid.enabled=true

# Or override via command line
mvn clean test -Dgrid.enabled=true
```

### Issue 4: Docker Container Exits Immediately

**Solution:**
```bash
# Check logs
docker-compose logs selenium-hub
docker-compose logs chrome

# Increase shared memory size
shm_size: 2gb  # In docker-compose.yml
```

---

## 📊 Grid Monitoring

### View Grid Status
```bash
# Grid Status API
curl http://localhost:4444/status | python -m json.tool

# Grid Console (Browser)
http://localhost:4444/ui
```

### Check Active Sessions
```bash
# List active sessions
curl http://localhost:4444/sessions
```

---

## 🎯 Best Practices

1. **Use Docker Grid** for consistent environments
2. **Set appropriate timeouts** for Grid connections
3. **Use ThreadLocal** for parallel execution (already implemented)
4. **Monitor Grid resources** during test runs
5. **Scale nodes based on** test parallelization needs

---

## 📁 Quick Reference

| Task | Command |
|------|---------|
| Start Local Grid | `java -jar selenium-server-*.jar standalone` |
| Start Docker Grid | `docker-compose up -d` |
| Stop Docker Grid | `docker-compose down` |
| Check Grid Status | `curl http://localhost:4444/status` |
| Run on Grid | `mvn clean test` (with `grid.enabled=true`) |
| Run Locally | `mvn clean test` (with `grid.enabled=false`) |

---

## 🔗 Useful Links

- [Selenium Grid Documentation](https://www.selenium.dev/documentation/grid/)
- [Docker Selenium Images](https://github.com/SeleniumHQ/docker-selenium)
- [LambdaTest Selenium Grid](https://www.lambdatest.com/selenium-grid)
- [BrowserStack Selenium Grid](https://www.browserstack.com/guide/selenium-grid-tutorial)
