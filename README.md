# Messenger MVP

Мінімальний месенджер з:
- 🌍 Signaling server (Node.js + WebSocket)
- 📱 Android клієнт (Kotlin, Jetpack Compose, WebRTC)
- 🔀 P2P через Wi-Fi (mDNS) + fallback на Internet
- 🛡️ Готовність для SQLCipher (E2EE планується)

---

## 🚀 Запуск сервера

```bash
cd server
npm install
node server.js
```

Або через Docker (coturn):
```bash
cd server
docker compose up -d
```

Сервер буде на `http://localhost:8080`.

---

## 📱 Android клієнт

Відкрити `android/` в Android Studio.

- Мінімальна версія SDK: 24
- Пакет: `com.example.messenger`

Функціонал MVP:
- WebSocket підключення до signaling
- Створення PeerConnection через WebRTC
- DataChannel для текстових повідомлень
- mDNS для локальної мережі (без інтернету)

---

## 🧪 Тестування локально

1. Запусти `server/`.
2. Запусти додаток на двох телефонах (userId=alice, userId=bob).
3. Alice відправляє повідомлення → йде через DataChannel або через signaling.
4. Якщо немає інтернету, mDNS дозволяє знайти peer у Wi-Fi і налаштувати P2P.
