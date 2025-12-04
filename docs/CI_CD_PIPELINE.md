# CI/CD Pipeline Setup Guide

This document explains the GitHub Actions CI/CD pipeline implemented for the Eventix project.

## 📁 Pipeline Structure

```
.github/workflows/
├── ci.yml          # Main CI pipeline for building and testing all services
└── pr-checks.yml   # Additional checks for pull requests
```

## 🚀 How It Works

### 1. Main CI Pipeline (`ci.yml`)

**Triggers:**
- Every push to `main` or `develop` branches
- Every pull request to `main` or `develop` branches

**What it does:**
- ✅ Builds all 5 microservices in parallel
- ✅ Runs unit tests for each service
- ✅ Uploads JAR artifacts for deployment
- ✅ Provides a summary of all build results

**Jobs:**
- `build-user-service` - Builds User Service
- `build-event-service` - Builds Event Service
- `build-booking-service` - Builds Booking Service
- `build-search-service` - Builds Search Service
- `build-dashboard-service` - Builds Dashboard Service
- `ci-summary` - Aggregates results from all jobs

### 2. Pull Request Checks (`pr-checks.yml`)

**Triggers:**
- Every pull request

**What it checks:**
- ⚠️ Large files (>10MB)
- 🔒 Hardcoded passwords in configuration files
- 📏 PR size (warns if >50 files changed)
- 🎨 Code style with Checkstyle
- 📝 TODO/FIXME comments

## 📊 Viewing Pipeline Results

### In GitHub:
1. Go to your repository on GitHub
2. Click the **Actions** tab
3. Select a workflow run to see details

### Build Status Badge:
The README includes a build status badge that shows:
- ✅ Green: All checks passing
- ❌ Red: Some checks failing
- 🟡 Yellow: Checks running

## 🔧 Running Locally

To test what the CI pipeline does locally:

```bash
# Navigate to a service
cd srv/user-service

# Build and test (same as CI)
./mvnw clean install
./mvnw test
```

## 🛠️ Customization

### Adding a New Service:
1. Open `.github/workflows/ci.yml`
2. Copy an existing job (e.g., `build-user-service`)
3. Rename it and update the paths
4. Add to the `needs` array in `ci-summary`

### Modifying Checks:
Edit the respective workflow files:
- Build checks → `ci.yml`
- PR validation → `pr-checks.yml`

## 📋 Common Issues

### Maven Wrapper Not Found
**Error:** `./mvnw: No such file or directory`

**Solution:** Ensure each service has Maven wrapper files:
```bash
cd srv/your-service
mvn wrapper:wrapper
```

### Tests Failing in CI
**Error:** Tests pass locally but fail in CI

**Common causes:**
- Hardcoded paths (use relative paths)
- Database connection issues (use H2 in-memory for tests)
- Timezone differences (use UTC in tests)

### Artifact Upload Fails
**Error:** No files found matching pattern

**Solution:** Check that the JAR is being created:
```bash
ls -la srv/your-service/target/*.jar
```

## 🎯 Best Practices

1. **Always run tests before pushing:**
   ```bash
   mvn test
   ```

2. **Keep builds fast:**
   - Use Maven caching (`cache: maven` in workflow)
   - Run jobs in parallel

3. **Fix broken builds immediately:**
   - Broken main branch blocks everyone
   - Consider reverting the commit if fix takes time

4. **Review workflow logs:**
   - Helps understand failures
   - Look for patterns in flaky tests

## 📚 Learn More

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

---

**Last Updated:** December 2025
