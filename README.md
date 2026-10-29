# Simulador Bipartito: Algoritmo de Gale-Shapley

Esta aplicación desarrollada en **Android (Kotlin/Jetpack Compose)** es un entorno analítico y visual para resolver el problema del emparejamiento estable mediante el Algoritmo de Gale-Shapley (1962).

##   Características
- **Complejidad Escalable:** Permite configurar dimensiones de grafos desde $N=1$ hasta $N=12$ con renderizado adaptativo en pantalla.
- **Simulación Visual en Tiempo Real:** Dibuja la red bipartita utilizando curvas de Bézier dinámicas y clasifica los eventos lógicos (Propuesta, Biyección, Ruptura y Rechazo) con semántica de colores.
- **Fundamentación Matemática:** Incluye un módulo teórico sobre el contexto histórico y la demostración de convergencia polinomial $O(N^2)$.
- **Control de Ejecución:** Ajuste de velocidad de retardo (ms) y generación estocástica (aleatoria) de matrices de preferencia.

---

##  Manual de Instalación y Configuración

### Requisitos Previos
- Dispositivo Android con versión 7.0 (Nougat) o superior.

### Instalación Directa (APK)
1. Descarga el archivo `GaleShapley-GabrielGonzalez.apk` adjunto en este repositorio (o en la sección de Releases).
2. Transfiérelo a tu dispositivo Android.
3. Ejecuta el archivo. Si el sistema lo solicita, autoriza la opción **"Instalar aplicaciones de fuentes desconocidas"**.
4. Abre la aplicación "Gale-Shapley".

### Compilación desde Código Fuente
1. Clona este repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/GaleShapley-Android.git](https://github.com/TU_USUARIO/GaleShapley-Android.git)
