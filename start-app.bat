@echo off

java -jar artifacts/app-deadline.jar ^
  -P:jdbc.url=jdbc:mysql://localhost:3306/appdb ^
  -P:jdbc.user=appuser ^
  -P:jdbc.password=password123