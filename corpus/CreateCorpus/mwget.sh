#!/bin/bash


function get_fichier() {
	local i=0
	while test -e "../file$i.txt"
	do
		((i++))
	done	
	echo "../file$i.txt"
}

wget $1 -q -O - > "src.txt"

if grep "vice.com" - <<< $1
then
	node vice_html-to-text.js > $(get_fichier)
fi
if grep "konbini.com" - <<< $1
then
	node konbini_html-to-text.js > $(get_fichier)
fi
if grep "liberation.fr" - <<< $1
then
	node libe_html-to-text.js > $(get_fichier)
fi
if grep "lemonde.fr" - <<< $1
then
	node lemonde_html-to-text.js > $(get_fichier)
fi
