Voici le contenu prêt à être copié directement dans ton `README.md` sur GitHub :  

```markdown
# 📘 **Projet d'Expérimentation d'Algorithmes**  

Ce projet compare l'algorithme de **Welzl** avec une **approche naïve** sur plusieurs ensembles de données et génère des courbes d'analyse des performances.

---

## 📋 **Prérequis**

- **Java** : Version **21 ou supérieure**  
- **Ant** : Outil de construction Apache Ant (nécessaire pour certaines tâches)  
- **Système** : Compatible **Linux**, **macOS**, ou **WSL** sous Windows  

### ✅ **Vérifier l'installation**
```bash
java -version
ant -version
```

## ⚙️ Configurations
 Quand vous ouvrirez le projet dans eclipse vous devez d'abord configurer les jars en suivant les étapes suivantes:
 - Faites un clic droit sur le nom du projet dans l'explorateur de projets (Project Explorer).
 - Sélectionnez Properties.
 - Dans la fenêtre qui s'ouvre, allez dans Java Build Path (dans la section de gauche).
 - Cliquez sur l'onglet Libraries.
 - Sélectionner les trois fichiers existant dans Classpath et faites remove
 - Sélectionner Classpath.
 - Cliquez sur Add External JARs...
 - Parcourez vos dossiers, sélectionnez le fichier JARS et séléctionner les trois jars qui se trouve dedans, puis cliquez sur OK.
 - Cliquez sur Apply and Close pour sauvegarder les modifications.

---

## 📁 **Structure du projet**
```
.
├── Makefile                # Automatisation des commandes
├── src/                    # Code source
│    ├── algorithms/        # Implémentation des algorithmes
│    └── Experimentation/   # Classes principales (exécutions et courbes)
├── jars/                   # Dépendances (fichiers .jar)
├── samples/                # Ensemble de données d'entrée
├── samplesDiff/            # Ensemble de données d'entrée alternatifs
├── Resultat/               # Résultats des expérimentations (fichiers CSV)
└── build.xml               # Script Ant pour la construction
```

---

## ▶️ **Instructions d'exécution**

### 1️⃣ **Compiler le projet**
```bash
make compile
```

### 2️⃣ **Exécuter l'expérimentation**
| **Commande**        | **Description**                                      |
|---------------------|-----------------------------------------------------|
| `make run_samples`  | Exécute l'expérimentation sur `samples/` et produit un CSV. |
| `make run_samplesDiff` | Exécute l'expérimentation sur `samplesDiff/`.         |

### 3️⃣ **Générer les courbes**
| **Commande**           | **Description**                                      |
|------------------------|-----------------------------------------------------|
| `make curve_samples`    | Produit une courbe de performance sur `samples/`.   |
| `make curve_samplesDiff`| Produit une courbe sur `samplesDiff/`.              |

### 4️⃣ **Utiliser Ant pour la construction**
| **Commande**      | **Description**                               |
|-------------------|----------------------------------------------|
| `make ant_build`  | Appelle le script `build.xml` via **Ant**.    |
| `make run_build`  | Lance l'exécution après la construction avec **Ant**. |

### 5️⃣ **Exécuter tout le processus**
```bash
make all
```

### 6️⃣ **Nettoyer le projet**
```bash
make clean
```

---

## 📊 **Exemple de workflow complet**
```bash
make run_samples       # Exécuter sur samples/
make curve_samples     # Générer la courbe pour samples/
make all               # Tout exécuter (compilation, tests, courbes, Ant)
make clean             # Nettoyer les fichiers générés
```

---

## 🛠️ **Dépannage**

- **Erreur `Command not found`** : Assurez-vous que **Java 21+** et **Ant** sont installés et accessibles depuis le `PATH`.  
  Vérifiez l'installation :
  ```bash
  java -version
  ant -version
  ```

- **Problème de compilation** : Vérifiez que le dossier `jars/` contient les dépendances nécessaires.
