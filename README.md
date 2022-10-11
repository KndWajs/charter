# Charter app

## 1. Run application for local development

### Start back-end (`server`)

```bash
gradle bootRun
```
- Or run Java with profile dev.
- Dev is using in memory H2 database filed up when starting application.
- By default, back-end use port 8055
- Java 17

One service `PaymentService` is currently fully implemented and tested.
`"/api/charter/create-charter"` is an endpoint to test payment functionality.

### Start front-end (`client`)

```bash
npm start
```

Front-end is currently prepared to test back-end and to have template for future development.
