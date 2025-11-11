## API Endpoints

Base URL: /api/v1

All responses return:
```json
{
  "message": "Action completed successfully",
  "data": { ... }
}
```


### AuthController, /auth
| Method | Endpoint  | Description                   | Request Body                    | Auth | Roles           |
| :----- | :-------- | :---------------------------- | :------------------------------ | :--- | :-------------- |
| `POST` | `/login`  | Log in a user                 | LoginRequest | ❌    | Public          |
| `POST` | `/logout` | Logout the authenticated user | None                            | ✅    | `USER`, `ADMIN` |

### ExerciseController, /exercises
| Method   | Endpoint                      | Description                     | Request Body                                      | Auth | Roles           |
| :------- | :---------------------------- | :------------------------------ | :------------------------------------------------ | :--- | :-------------- |
| `GET`    | `/all`                        | Get all global + user exercises | None                                              | ✅    | `USER`, `ADMIN` |
| `GET`    | `/exercise/{id}/exercise`     | Get exercise by ID              | None                                              | ✅    | `USER`, `ADMIN` |
| `POST`   | `/add/user/exercise`          | Add user-specific exercise      | AddExerciseRequest       | ✅    | `USER`          |
| `POST`   | `/add/global/exercise`        | Add global exercise             | AddExerciseRequest       | ✅    | `ADMIN`         |
| `DELETE` | `/exercise/{id}/admin/delete` | Delete exercise globally        | None                                              | ✅    | `ADMIN`         |
| `DELETE` | `/exercise/{id}/user/delete`  | Delete user exercise            | None                                              | ✅    | `USER`          |
| `PUT`    | `/exercise/{id}/user/update`  | Update user exercise            | UpdateExerciseRequest | ✅    | `USER`          |
| `PUT`    | `/exercise/{id}/admin/update` | Update global exercise          | UpdateExerciseRequest | ✅    | `ADMIN`         |

### WorkoutController, /workouts
| Method   | Endpoint                    | Description                        | Request Body                                                    | Auth | Roles           |
| :------- | :-------------------------- | :--------------------------------- | :-------------------------------------------------------------- | :--- | :-------------- |
| `GET`    | `/all`                      | Get all workouts for user or admin | None                                                            | ✅    | `USER`, `ADMIN` |
| `POST`   | `/add/user/workout`         | Create a user workout template     | CreateWorkoutTemplateRequest | ✅    | `USER`          |
| `PUT`    | `/workout/{id}/user/update` | Update a user workout template     | CreateWorkoutTemplateRequest | ✅    | `USER`          |
| `DELETE` | `/workout/{id}/user/delete` | Delete a user workout template     | None                                                            | ✅    | `USER`          |

### HistoryController, /history
| Method   | Endpoint                                              | Description                         | Request Body                                                      | Auth | Roles  |
| :------- | :---------------------------------------------------- | :---------------------------------- | :---------------------------------------------------------------- | :--- | :----- |
| `GET`    | `/all/user/completed-workouts`                        | Get all completed workouts for user | None                                                              | ✅    | `USER` |
| `POST`   | `/save/user/completed-workout`                        | Save a completed workout            | SaveCompletedWorkoutRequest    | ✅    | `USER` |
| `PUT`    | `/update/user/completed-workout/{completedWorkoutId}` | Update completed workout            | UpdateCompletedWorkoutRequest | ✅    | `USER` |
| `DELETE` | `/delete/user/completed-workout/{completedWorkoutId}` | Delete completed workout            | None                                                              | ✅    | `USER` |

### UserController, /users
| Method   | Endpoint           | Description         | Request Body                              | Auth | Roles                  |
| :------- | :----------------- | :------------------ | :---------------------------------------- | :--- | :--------------------- |
| `GET`    | `/{userId}/user`   | Get user by ID      | None                                      | ✅    | `ADMIN`, `USER (self)` |
| `POST`   | `/create`          | Create new user     | CreateUserRequest | ❌    | Public                 |
| `PUT`    | `/{userId}/update` | Update user profile | UpdateUserRequest | ✅    | `USER (self)`, `ADMIN` |
| `DELETE` | `/{userId}/delete` | Delete user         | None                                      | ✅    | `USER (self)`, `ADMIN` |

## Request DTO Schemas

### LoginRequest
```json
{
  "email": "string",
  "password": "string"
}
```

### AddExerciseRequest
```json
{
  "id": 1,
  "name": "Bench Press",
  "description": "Chest pressing exercise",
  "primaryMuscleGroup": "CHEST",
  "secondaryMuscleGroup": "TRICEPS",
  "exerciseType": "STRENGTH"
}
```

### UpdateExerciseRequest
(Same as AddExerciseRequest)
```json
{
  "id": 2,
  "name": "Incline Bench Press",
  "description": "Targets upper chest",
  "primaryMuscleGroup": "CHEST",
  "secondaryMuscleGroup": "SHOULDERS",
  "exerciseType": "STRENGTH"
}
```
### SaveCompletedWorkoutRequest

```json
{
  "completedAt": "2025-11-10",
  "durationSeconds": 3600,
  "notes": "Felt strong today",
  "sets": [
    {
      "exerciseId": 12,
      "orderNumber": 1,
      "restSeconds": 90,
      "reps": 8,
      "weight": 100
    }
  ]
}
```
### UpdateCompletedWorkoutRequest
```json
{
  "id": 5,
  "completedAt": "2025-11-10",
  "durationSeconds": 3550,
  "notes": "Updated notes",
  "sets": [
    {
      "setId": 14,
      "exerciseId": 12,
      "orderNumber": 1,
      "restSeconds": 90,
      "reps": 9,
      "weight": 102
    }
  ]
}
```
### CreateUserRequest
```json
{
  "username": "alberth",
  "email": "alberth@example.com",
  "password": "securepassword"
}
```
### CreateWorkoutTemplateRequest
```json
{
  "id": 1,
  "name": "Push Day",
  "description": "Chest, shoulders, triceps workout",
  "workoutExercises": [
    {
      "exerciseId": 10,
      "orderNumber": 1,
      "restSeconds": 120,
      "workoutSets": [
        { "reps": 10, "weight": 80.0 },
        { "reps": 8, "weight": 85.0 }
      ]
    }
  ]
}
```
### CreateWorkoutExerciseRequest
```json
{
  "exerciseId": 10,
  "orderNumber": 1,
  "restSeconds": 90,
  "workoutSets": [
    { "reps": 8, "weight": 100.0 },
    { "reps": 6, "weight": 110.0 }
  ]
}

```
### CreateWorkoutSetRequest
```json
{
  "reps": 8,
  "weight": 100.0
}

```

```json
```

## Database Diagram
<img width="984" height="748" alt="image" src="https://github.com/user-attachments/assets/17bcee1f-400a-4c65-9fa5-d077e11bbfe6" />
