#!/bin/bash

prompt="Select a profile:"

options=($(cat ~/.aws/credentials |grep '\[.*\]'|sed -e 's/\[\|\]//g'))
options_len=${#options[@]}
selected=""

str='select opt in "${options[@]}" "Quit"; do\n'
str+='  case "''$REPLY''" in\n'

for (( j=0; j < ${options_len}; j++ ));
do
k="$((j+1))"
  str+='    '"$k)"
  str+='      '"selected=${options[$j]}; break\n"
  str+='    ;;\n'
done

str+='    '"$((${#options[@]}+1))) echo "Goodbye!"; exit 0; break;;\n"
str+='    ''*) echo "Invalid index"; continue;;\n'
str+='  esac\n'
str+='done\n'

#echo -e $str

p=`echo -e $str`

eval "$p"

echo $selected

