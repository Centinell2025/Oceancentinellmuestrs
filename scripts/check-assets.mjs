import {readdirSync,readFileSync} from 'node:fs';
import assert from 'node:assert/strict';
const src=new URL('../dist/',import.meta.url),dst=new URL('../android/app/src/main/assets/',import.meta.url);
for(const entry of readdirSync(src,{withFileTypes:true}))if(entry.isFile())assert.deepEqual(readFileSync(new URL(entry.name,src)),readFileSync(new URL(entry.name,dst)),entry.name+' is not synchronized');
console.log('All Android interface assets match the reviewed source.');
