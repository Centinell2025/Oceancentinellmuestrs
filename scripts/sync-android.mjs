import {readdirSync,mkdirSync,copyFileSync} from 'node:fs';
import {fileURLToPath} from 'node:url';
const src=new URL('../dist/',import.meta.url),dst=new URL('../android/app/src/main/assets/',import.meta.url);
mkdirSync(dst,{recursive:true});
for(const entry of readdirSync(src,{withFileTypes:true}))if(entry.isFile())copyFileSync(new URL(entry.name,src),new URL(entry.name,dst));
console.log('Android assets synchronized from '+fileURLToPath(src));
