self.addEventListener("push", (event) => {
  const title = event.data.text();

  event.waitUntil(self.registration.showNotifcation(title));
});
Notification("Test Notifcation!", {
  body: "body",
  icon: "../public/vite.svg",
  actions: [
    {
      action: "do-Somthing",
      title: "doSomthing!",
      type: "button",
    },
  ],
});