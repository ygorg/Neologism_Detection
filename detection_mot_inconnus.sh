#!/bin/bash

function filter() {
	while read mot
	do
		((${#mot} <= 2)) || [[ $mot =~ ^-?[0-9]+$ ]] || echo $mot
	done		
}


if (( "$#" != 1 ))
then
	echo "Usage: ./detection_mot_inconnus.sh file"
	exit
fi

mkdir output 2> "/dev/null"

file="$1"

output="output"
ressources="ressources"
bin="bin"

tokenised="$output/$$-tokenised.tmp"
stop_word="$output/$$-stop_word.tmp"
numbers="$output/$$-numbers.tmp"
candidates="$output/$$-candidates.tmp"

java_cp="$bin:$bin/radixtree"

dela="$ressources/dela-fr-public-u8.dic.xml"
dela_type="$ressources/dela-fr-public-u8.type.txt"

cat "$file" | "$bin/tokenizer.perl" -l fr 2> /dev/null | tr ' ' '\n' | sort | uniq > "$tokenised"

java -cp "$java_cp" ExistingWord -s -d "$ressources/lexicon.txt" - < "$tokenised" > "$stop_word"


#On garde seulement les mots ayant une longueur supérieur à 2
#On enleve les mots qui sont des nombres
filter < "$stop_word" > "$numbers"

#grep 4s, java 2s
#print unknown word from dict to stdout
if test ! -e "$dela_type"
then
	grep form < "$dela" | sed 's/.*>\([^<]*\)<.*/\1/g' | tr ' ' '\n' | tr -d '\\' | sort | uniq > "$dela_type"
fi

java -cp "$java_cp" ExistingWord -s -d "$dela_type" - < "$numbers" > "$candidates"

cat "$candidates"

rm "$tokenised" "$stop_word" "$numbers" "$candidates"

