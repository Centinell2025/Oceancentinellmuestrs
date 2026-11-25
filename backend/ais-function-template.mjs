import WebSocket from 'ws@8.23.0';
// AIS_CORE is inserted by the build script. Only this generated file is deployed.
/* AIS_CORE */
const privacyDocument=/* PRIVACY_DOCUMENT */;
const box=DEFAULT_BOX,records=new Map(),rates=new Map();
let ws=null,connected=false,lastMessageAt=null,lastClient=0,retry=null,attempt=0,errorCode=null;
const key=Bun.env.AISSTREAM_API_KEY;
function active(){return Date.now()-lastClient<120000}
function connect(){
  if(!key||ws||!active())return;
  errorCode=null;
  const socket=new WebSocket('wss://stream.aisstream.io/v0/stream',{perMessageDeflate:true,maxPayload:1024*1024,handshakeTimeout:12000});ws=socket;
  socket.on('open',()=>socket.send(JSON.stringify({APIKey:key,BoundingBoxes:[[[box[0],box[1]],[box[2],box[3]]]],FilterMessageTypes:['PositionReport','StandardClassBPositionReport','ExtendedClassBPositionReport']})));
  socket.on('message',raw=>{try{const event=JSON.parse(raw.toString());if(event.error||event.Error){errorCode='provider_rejected';socket.close();return}if(event.MessageType==='SubscriptionConfirmation'){connected=true;attempt=0;return}const p=normalizePosition(event);if(!p)return;connected=true;attempt=0;lastMessageAt=p.receivedAt;records.delete(p.mmsi);records.set(p.mmsi,p);while(records.size>1000)records.delete(records.keys().next().value)}catch{errorCode='invalid_provider_message'}});
  socket.on('error',()=>{errorCode='provider_unavailable'});
  socket.on('close',()=>{if(ws!==socket)return;ws=null;connected=false;if(active()&&errorCode!=='provider_rejected'){const delay=Math.min(60000,1000*2**Math.min(attempt++,6))+Math.random()*1000;retry=setTimeout(connect,delay)}});
  function idleCheck(){if(ws!==socket)return;if(!active())socket.close();else setTimeout(idleCheck,60000)}setTimeout(idleCheck,125000);
}
function json(data,status=200){return Response.json(data,{status,headers:{'Cache-Control':'no-store','X-Content-Type-Options':'nosniff'}})}
const server=Bun.serve({port:Number(Bun.env.PORT||3000),fetch(req){const url=new URL(req.url),origin=req.headers.get('Origin');const respond=(data,status=200)=>{const r=json(data,status);if(['https://oceancentinell-b2b-demo.centinellsuite.chatgpt.site','http://127.0.0.1:4173'].includes(origin)){r.headers.set('Access-Control-Allow-Origin',origin);r.headers.set('Vary','Origin')}return r};if(url.pathname==='/privacy'&&req.method==='GET')return new Response(privacyDocument,{headers:{'Content-Type':'text/html; charset=utf-8','Cache-Control':'public, max-age=300','X-Content-Type-Options':'nosniff','Content-Security-Policy':"default-src 'none'; style-src 'unsafe-inline'; base-uri 'none'; form-action 'none'"}});if(req.method!=='GET')return respond({error:'method_not_allowed'},405);if(url.pathname==='/health')return respond({service:'OceanCentinell AIS gateway',configured:!!key,provider:'AISStream'});
  if(url.pathname!=='/positions')return respond({error:'not_found'},404);
  const ip=server.requestIP(req)?.address||'unknown',now=Date.now(),minute=Math.floor(now/60000);let rate=rates.get(ip);if(!rate||rate.minute!==minute){rate={minute,count:0};rates.set(ip,rate)}if(++rate.count>30)return respond({error:'rate_limited'},429);if(rates.size>10000){for(const [i,v]of rates)if(v.minute!==minute)rates.delete(i);while(rates.size>10000)rates.delete(rates.keys().next().value)}
  lastClient=now;connect();
  return respond({provider:'AISStream',configured:!!key,connected,lastMessageAt,error:!key?'provider_key_required':errorCode,bounds:box,generatedAt:new Date(now).toISOString(),positions:visiblePositions(records,now),scope:'Public regional AIS observations. Not a customer fleet. No navigation or delivery guarantee.'});
}});
