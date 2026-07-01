// src/main/resources/static/assets/app.js
function encryptMessage(text) {
  return btoa(text); 
}

function decryptMessage(cipher) {
  try { return atob(cipher); } catch { return cipher; }
}


async function apiRequest(url, method="GET", body=null) {
  const opts = { method, headers: { "Content-Type": "application/json" } };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(url, opts);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
