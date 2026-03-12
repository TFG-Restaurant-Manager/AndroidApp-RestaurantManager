# Restaurant Manager Mobile App (ES)

### Español es / [English en](README.md)

Aplicación móvil para el personal del restaurante que se conecta a la **Restaurant Manager API**.
La aplicación está pensada principalmente para **camareros**, permitiéndoles gestionar mesas, pedidos y su información personal.

---

## Funcionalidades

- Visualizar y gestionar **mesas del restaurante**
- Crear y gestionar **pedidos**
- Consultar **comida y productos disponibles**
- Ver el **perfil personal**
- Consultar **horarios de trabajo**

---

## Tecnologías

- **Kotlin**
- **Android / Jetpack Compose**
- **Integración con API REST**
- **Arquitectura MVVM**

---

## Pantallas

### Perfil

Muestra la información personal del empleado y su calendario de trabajo.

![Pantalla Perfil](images/profile.png)

---

### Pedidos

Permite a los camareros crear y gestionar pedidos de los clientes.

![Pantalla Pedidos](images/orders.png)

---

### Mesas

Muestra las mesas del restaurante y su estado actual.

![Pantalla Mesas](images/tables.png)

---

### Comida

Lista de platos y productos disponibles para añadir a un pedido.

![Pantalla Comida](images/food.png)

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/your-repository/restaurant-manager-mobile.git
cd restaurant-manager-mobile
```

---

### 2. Abrir el proyecto

Abrir el proyecto con **Android Studio**.

---

### 3. Configurar la conexión con la API

Editar la URL base en el archivo de configuración de la API:

```kotlin
BASE_URL = "http://localhost:8080"
```

Si utilizas un emulador de Android:

```kotlin
BASE_URL = "http://10.0.2.2:8080"
```

---

### 4. Ejecutar la aplicación

Conectar un dispositivo o iniciar un emulador y ejecutar el proyecto desde **Android Studio**.

---

## Estructura del proyecto

```
AndroidApp_RestaurantManager
    └── app/src
        ├── androidTest
        ├── test
        └── java/com/tfg_rm/androidapp_restaurantmanager
            ├── data
            │   ├── remote
            │   └── repository
            ├── ui
            │   ├── screens
            │   ├── theme
            │   └── navigation
            ├── domain
            |   ├── models
            |   ├── services
            |   └── viewmodels
            └── MainActivity.kt
```

---

## Dependencia del Backend

Esta aplicación depende del backend **Restaurant Manager API**.

Asegúrate de que el backend esté en ejecución antes de iniciar la aplicación móvil.
