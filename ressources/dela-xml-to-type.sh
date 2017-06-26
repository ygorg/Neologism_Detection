
grep form < "dela-fr-public-u8.dic.xml" | sed 's/.*>\([^<]*\)<.*/\1/g' | tr ' ' '\n' | tr -d '\\' | sort | uniq > dela-2.txt
