plugins {
    java
}

repositories {
    mavenCentral() // Assurez-vous que le dépôt Maven Central est inclus pour télécharger les dépendances
}

dependencies {
    // Assurez-vous que cette ligne est à l'intérieur du bloc `dependencies`
    testImplementation("com.approvaltests:approvaltests:5.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1") // Utilisez la dernière version disponible
}
