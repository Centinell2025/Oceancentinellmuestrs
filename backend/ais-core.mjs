export const DEFAULT_BOX=[24,-85,34,-78];
export function normalizePosition(event,receivedAt=Date.now(),box=DEFAULT_BOX){
  const type=event?.MessageType;
  if(!['PositionReport','StandardClassBPositionReport','ExtendedClassBPositionReport'].includes(type))return null;
  const p=event.Message?.[type],m=event.MetaData;
  if(!p||!m||p.Valid===false)return null;
  const mmsi=String(m.MMSI??p.UserID??'');
  const lat=p.Latitude??m.latitude??m.Latitude,lon=p.Longitude??m.longitude??m.Longitude;
  if(!/^\d{9}$/.test(mmsi)||!Number.isFinite(lat)||!Number.isFinite(lon)||lat<box[0]||lat>box[2]||lon<box[1]||lon>box[3])return null;
  const rawTime=typeof m.time_utc==='string'?Date.parse(m.time_utc.replace(/ UTC$/,'')):NaN;
  if(Number.isFinite(rawTime)&&(rawTime>receivedAt+60000||rawTime<receivedAt-1800000))return null;
  return {mmsi,name:String(m.ShipName||'AIS vessel').replace(/[\u0000-\u001f\u007f]/g,'').trim().slice(0,100),lat,lon,speedKnots:Number.isFinite(p.Sog)&&p.Sog>=0&&p.Sog<102.3?p.Sog:null,courseDegrees:Number.isFinite(p.Cog)&&p.Cog>=0&&p.Cog<360?p.Cog:null,receivedAt:new Date(receivedAt).toISOString(),providerTimestamp:Number.isFinite(rawTime)?new Date(rawTime).toISOString():null,source:'AISStream'};
}
export function visiblePositions(records,now=Date.now()){
  return [...records.values()].filter(p=>{const age=now-Date.parse(p.receivedAt);return age>=0&&age<=1800000}).map(p=>({...p,ageSeconds:Math.floor((now-Date.parse(p.receivedAt))/1000),stale:now-Date.parse(p.receivedAt)>180000})).sort((a,b)=>a.ageSeconds-b.ageSeconds).slice(0,500);
}
