import {readFileSync,writeFileSync} from 'node:fs';
const core=readFileSync(new URL('./ais-core.mjs',import.meta.url),'utf8').replace(/^export /gm,'');
const template=readFileSync(new URL('./ais-function-template.mjs',import.meta.url),'utf8');
const policy=readFileSync(new URL('../play-store/PRIVACY.md',import.meta.url),'utf8');
const html='<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>OceanCentinell Business Privacy Policy</title><body style="max-width:850px;margin:40px auto;padding:20px;font:17px/1.7 Arial;color:#173c54"><article style="white-space:pre-wrap">'+policy.replace(/[&<>]/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;'}[c]))+'</article></body></html>';
writeFileSync(new URL('./ais-function.ts',import.meta.url),template.replace('/* AIS_CORE */',core).replace('/* PRIVACY_DOCUMENT */',JSON.stringify(html)));
