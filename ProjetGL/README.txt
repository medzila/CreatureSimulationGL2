Projet / TP LONG sur le chargement dynamique  

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
nom et d'autre part car tous notre code est écrut en anglais.

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


Utilisation des fichiers de config: