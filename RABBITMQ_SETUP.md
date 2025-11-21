# RabbitMQ Integration Setup

## Overview
The booking service now publishes booking notifications to a RabbitMQ queue, and the notification service (dashboard-service) consumes these messages to send email notifications.

## Architecture Flow

```
Booking Service (Producer) → RabbitMQ Queue → Notification Service (Consumer) → SendGrid Email
```

1. **Booking Service**: When a booking is created, it publishes a notification message to RabbitMQ
2. **RabbitMQ**: Acts as a message broker, storing messages in the `booking.notifications.q` queue
3. **Notification Service**: Listens to the queue and sends confirmation emails via SendGrid

## Prerequisites

### 1. Install and Start RabbitMQ

**Windows (using Chocolatey):**
```cmd
choco install rabbitmq
```

**Or download from:** https://www.rabbitmq.com/download.html

**Start RabbitMQ:**
```cmd
rabbitmq-server
```

**Access RabbitMQ Management UI:**
- URL: http://localhost:15672
- Username: `guest`
- Password: `guest`

### 2. Configure Services

Both services are already configured to connect to RabbitMQ on `localhost:5672`.

**Booking Service** (`srv/booking-service/src/main/resources/application.yml`):
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

**Notification Service** (`srv/dashboard-service/src/main/resources/application.properties`):
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## How It Works

### Message Structure

The notification message has the following structure:
```json
{
  "userEmail": "user@example.com",
  "userName": "John Doe",
  "eventName": "Spring Boot Workshop",
  "bookingId": "abc-123-def"
}
```

### Booking Service (Producer)

When a booking is created via:
- `POST /api/bookings` (synchronous)
- `POST /api/bookings/async` (asynchronous)

The service automatically:
1. Saves the booking to the database
2. Publishes a notification message to the `booking.notifications.q` queue

**Key Classes:**
- `RabbitMQConfig.java`: Configures the queue and message converter
- `NotificationPublisher.java`: Service to publish messages
- `BookingService.java`: Updated to call the publisher after creating bookings

### Notification Service (Consumer)

The notification service:
1. Listens to the `booking.notifications.q` queue
2. Receives messages automatically
3. Sends confirmation emails via SendGrid

**Key Classes:**
- `RabbitMQConfig.java`: Configures the queue listener
- `NotificationConsumer.java`: Handles incoming messages
- `EmailService.java`: Sends emails using SendGrid

## Testing the Integration

### Step 1: Start RabbitMQ
```cmd
rabbitmq-server
```

### Step 2: Start Notification Service (Dashboard Service)
```cmd
cd srv\dashboard-service
mvnw spring-boot:run
```

The service will:
- Connect to RabbitMQ
- Start listening to the `booking.notifications.q` queue
- Wait for messages

### Step 3: Start Booking Service
```cmd
cd srv\booking-service
mvnw spring-boot:run
```

### Step 4: Create a Booking

**Using Swagger UI:**
1. Navigate to http://localhost:8085/swagger-ui.html
2. Find the `POST /api/bookings` endpoint
3. Click "Try it out"
4. Enter booking details:
```json
{
  "eventId": "event-123",
  "userId": "user-456",
  "city": "New York",
  "category": "Technology"
}
```
5. Click "Execute"

**Using curl:**
```cmd
curl -X POST http://localhost:8085/api/bookings ^
  -H "Content-Type: application/json" ^
  -d "{\"eventId\":\"event-123\",\"userId\":\"user-456\",\"city\":\"New York\",\"category\":\"Technology\"}"
```

### Step 5: Verify Message Flow

**Check Booking Service Logs:**
```
📤 Publishing booking notification to queue: booking.notifications.q
✅ Booking notification published successfully for booking ID: abc-123-def
```

**Check Notification Service Logs:**
```
=== Message Received from Queue ===
Booking ID: abc-123-def
User Email: user-456@example.com
...
✅ Notification processed successfully for booking ID: abc-123-def
```

**Check RabbitMQ Management UI:**
- Go to http://localhost:15672
- Click "Queues" tab
- You should see `booking.notifications.q` with message statistics

## Troubleshooting

### RabbitMQ Connection Failed
**Error:** `Connection refused: connect`

**Solution:**
1. Make sure RabbitMQ is running: `rabbitmq-server`
2. Check if port 5672 is open
3. Verify credentials (default: guest/guest)

### Messages Not Being Consumed
**Problem:** Messages pile up in the queue but aren't processed

**Solution:**
1. Make sure notification service is running
2. Check notification service logs for errors
3. Verify SendGrid API key is configured correctly

### SendGrid Email Sending Failed
**Error:** `Email sending failed`

**Solution:**
1. Verify SendGrid API key in `application.properties`
2. Make sure sender email is verified in SendGrid dashboard
3. Check SendGrid account status and quotas

## Important Notes

### Current Limitations
1. **User Email & Event Name**: Currently using placeholder values based on IDs
   - Actual implementation should fetch from user-service and event-service
   - Update `BookingService.publishBookingNotification()` method

2. **Email Verification**: Make sure to verify your sender email in SendGrid:
   - Go to SendGrid Dashboard → Settings → Sender Authentication
   - Verify your email address
   - Update `sendgrid.from.email` in `application.properties`

### Production Considerations
1. **Error Handling**: Failed messages are requeued automatically
2. **Dead Letter Queues**: Consider adding DLQ for permanently failed messages
3. **Monitoring**: Use RabbitMQ management UI or monitoring tools
4. **Scaling**: Multiple notification service instances can process messages in parallel
5. **Security**: Use environment variables for RabbitMQ credentials in production

## Service URLs

- **Booking Service**: http://localhost:8085
- **Booking Service Swagger**: http://localhost:8085/swagger-ui.html
- **Notification Service**: http://localhost:8085 (same port, different app)
- **RabbitMQ Management**: http://localhost:15672
- **GraphiQL (Booking)**: http://localhost:8085/graphiql

## Files Modified/Created

### Booking Service
- ✅ `pom.xml` - Added RabbitMQ dependency
- ✅ `application.yml` - Added RabbitMQ configuration
- ✅ `config/RabbitMQConfig.java` - Queue and converter configuration (NEW)
- ✅ `dto/NotificationMessage.java` - Message DTO (NEW)
- ✅ `service/NotificationPublisher.java` - Message publisher service (NEW)
- ✅ `service/BookingService.java` - Updated to publish notifications

### Notification Service
- ℹ️ Already configured with RabbitMQ consumer
- ℹ️ No changes needed

## Next Steps

1. **Integrate with User Service**: Fetch actual user email and name
2. **Integrate with Event Service**: Fetch actual event details
3. **Add Email Templates**: Create HTML email templates
4. **Add Retry Logic**: Implement exponential backoff for failed messages
5. **Add Monitoring**: Implement metrics and alerting

