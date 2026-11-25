import {readFileSync,writeFileSync} from 'node:fs';
const core=readFileSync(new URL('./ais-core.mjs',import.meta.url),'utf8').replace(/^export /gm,'');
const template=readFileSync(new URL('./ais-function-template.mjs',import.meta.url),'utf8');
writeFileSync(new URL('./ais-function.ts',import.meta.url),template.replace('/* AIS_CORE */',core));
