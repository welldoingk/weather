import webpush from "web-push";

// VAPID keys should be generated only once.
const vapidKeys = webpush.generateVAPIDKeys();
console.log(vapidKeys);
