import { useEffect } from "react";

// useEffect(() =>
// );

if ("serviceWorker" in navigator) {
  navigator.serviceWorker.register("my-sw.js");
}
function App() {
  return <button>구도구도구독</button>;
}
export default App;
