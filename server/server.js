const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();
app.use(cors());
app.use(bodyParser.json());

const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

const clients = new Map();

wss.on('connection', (ws) => {
  ws.on('message', (msgRaw) => {
    try {
      const msg = JSON.parse(msgRaw);
      if (msg.type === 'auth') {
        ws.userId = msg.userId;
        clients.set(msg.userId, ws);
        console.log('auth', msg.userId);
        return;
      }
      if (msg.type === 'signal') {
        const peer = clients.get(msg.to);
        if (peer && peer.readyState === WebSocket.OPEN) {
          peer.send(JSON.stringify({ type: 'signal', from: ws.userId, payload: msg.payload }));
        }
      }
    } catch (e) { console.error('ws parse err', e); }
  });

  ws.on('close', () => {
    if (ws.userId) clients.delete(ws.userId);
  });
});

app.get('/', (req, res) => res.send('Signaling server running'));

const PORT = process.env.PORT || 8080;
server.listen(PORT, () => console.log('Signaling server on', PORT));