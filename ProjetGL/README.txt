Projet / TP LONG sur la Variabilité

Feature Models:

Les features models sont dans le dossier src/commons et le fichier se nomme simu.fml

Implementation:

Le feature model d'implementation va essayer de coller le plus pres au code possible.
Ainsi nous avons trois grande features : les creatures, les points d'energie et le moteur.
Dans la feature creature qui est obligatoire, on va donner le maximum de details concernant tous les aspects des creatures 
(largeur du champ de vision, distance de vue, nombres, comportement, deplacement, couleurs).
Ensuite nous avons la features points d'energie qui est facultative, encore une fois on donne le maximum de détails
sur les points d'énergie (taille, nombre).
Et finalement la feature Engine, qui donnera des infos sur l'execution. Soit nous avons un déroulement en temps réel, soit nous avons un snapshot. Seul la feature visual est obligatoire car elle donne le type d'execution, ensuite si nous sommes en temps réel, on peut
forcer la valeur de la durée du Thread.sleep() pour avoir une éxecution plus ou moins rapide, et si nous sommes en snapshot, alors
nous mettons le Thread.sleep() a 0 et on choisi la durée de la simulation.

Toutes les features qui on un lien direct avec une variable dans le code sont de la forme "identifiant_valeur"
afin de facilité l'implementation du lien entre FM et code.
De plus tous le FM est écrit en anglais, d'une part pour facilité le pont entre les deux et empecher deux features d'avoir le même
nom et d'autre part car tous notre code est écrit en anglais.

Domaine:

Le feature model de domaine va essayer d'être comprehensible par quelqu'un qui doit utiliser le simulateur sans en connaitre les détails technique.
Ainsi nous avons toujours trois grande features mais plus exactement les mêmes: les créatures, l'affichage et les demos.
Dans la feature créature, nous savons simplifié le tout en ne mettant que le nombre et des types de créatures qui sont des comportement et des deplacements
afin de ne sortir du simulateur que des configurations comportmenent plus deplacement intéressant.
La feature affichage nous permet de choisir comme dans l'implementation les meme choix mais comprehensible par l'utilisateur et non seulement le
developpeur
Et pour finir la feature démo, qui permet de choisir des démos soit paramétrable, soit non paramétrable. Les choix sont aussi bien fixé dans le FM de domaine que d'implementation.

L'aggrégat:

L'aggrégat des deux FM se fait sans souis en liant principalement toutes les infos correspondantes (nombre de créature d'un FM a l'autre, type de créature a un comprtement et un deplacement), nous avons ensuite des démos qui vont uniquement fixé un environement avec plus ou moins de points d'energie et des tailles variable.
Nous obtenons bien à la fin trois comportement mort, ce qui était attendu vu qu'il ne sont utilisé par aucun type de créature et aucune démo.

Le pont:

Le pont entre le feature model et le code de la simulation se fait grâce à la classe ConfigHandler.
On récupère le fichier fml qu'on evalue. On évalue ensuite un fichier de configuration se trouvant dans le dossier Config/. Les fonctionnalités séléctionnées grâce à cette configuration sont parsées.
Le parsing se fait seulement sur les fonctionnalités ayant un "_". En effet, la partie avant de l'underscore est une clé contenue initialement dans un dictionnaire et la partie après l'underscore est la valeur de la clé en question.
Ce dictionnaire contient un ensemble de clé / valeur initialement définies ce qui permet une configuration par defaut.
Une fois ces étapes terminées, le dictionnaire mis à jour est transmis au Launcher qui crée la simulation correspondante aux fonctionnalités séléctionnées.
Par exemple:
	Si on veut appliquer la fonctionnalité "nombre de créature", on fait:
		launcher.nombreCreatures(dictionnaire.get("CHC"));

Rajouts dans le code:

-Snapshot: permet de n'afficher que l'état de la simulation après n ticks.
	Rajout d'un booleen isSnapshot dans le Simulator.
	Tant qu'on n'arrive pas à n ticks on n'affiche pas la simulation.
-Duration: nombre de ticks avant de prendre un snapshot.
	Rajout d'un nombre de ticks max dans le Simulator.
	On arrête la simulation lorsque les ticks actuels atteigne le nombre de ticks max.
-Affichage des valeurs de la configuration dans l'interface: il n'y a plus de slider où de choix à faire.


Utilisation des fichiers de config:

Les différents fichiers de config permettent de selectionner différents paramétres présent dans le domaine et lancer la simulation choisi parametrable ou non. Pour cela les fichiers de config contiennent des select permettant la selection de différents parametres dans le fichier liant le domaine et l'implémentation. Il y a 7 fichiers différents de configuation :
1) Immortels -> Démo non paramétrable 
2) MortIneluctable -> Demo non paramétrable
3) ModeleRecherche -> Demo semi-paramétrable
4) ModelePredateur -> Demo semi-parametrable
5) BcpEngSrc -> Demo parametrable avec Beaucoup de points d'energie.
6) PeuEngSrc -> Demo parametrable avec Peu de points d'energie.
7) PasEngSrc -> Demo parametrable avec aucun point d'energie.
