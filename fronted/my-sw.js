self.addEventListener("install", (e) => {
  console.log("[Service Worker] Install");
});

self.addEventListener("push", (event) => {
  console.log("[ServiceWorker] 푸시알림 수신: ", event);
  //Push 정보 조회
  var title = event.data.title || "알림";
  var body = event.data.body;
  var icon = event.data.icon || "/Images/icon.png"; //512x512
  var badge = event.data.badge || "/Images/badge.png"; //128x128
  var options = {
    body: body,
    icon: icon,
    badge: badge,
  };
  //Notification 출력
  event.waitUntil(self.registration.showNotification(title, options));
});

//사용자가 Notification을 클릭했을 때
self.addEventListener("notificationclick", (event) => {
  console.log("[ServiceWorker] 푸시알림 클릭: ", event);

  event.notification.close();
  event.waitUntil(
    self.clients.matchAll({ type: "window" }).then((clientList) => {
      //실행된 브라우저가 있으면 Focus
      for (var i = 0; i < clientList.length; i++) {
        var client = clientList[i];
        if (client.url == "/" && "focus" in client) return client.focus();
      }
      //실행된 브라우저가 없으면 Open
      if (self.clients.openWindow)
        return self.clients.openWindow("http://localhost:5173/");
    })
  );
});
console.log("[ServiceWorker] 시작");
