import axios from "axios";

if ("serviceWorker" in navigator) {
  navigator.serviceWorker.register("my-sw.js");
}

function subscription() {
  navigator.serviceWorker.ready.then((registration) => {
    registration.pushManager.getSubscription().then((subscription) => {
      if (subscription) {
        //DB에 저장
        axios.post("http://localhost:8080/push/subscribe");
      } else {
        registration.pushManager
          .subscribe({
            userVisibleOnly: true,
            applicationServerKey:
              "BCt8g0mhCW9uQlP7V_A2fjz_AGybu6uQG5ZrxGuft6LU97bvfTAwiQNBXg_uNMnx6ss_szKuPDZs1ofRiYJKYa0",
          })
          .then((subscription) => {
            //DB에 저장
          });
      }
    });
  });
}

function App() {
  return <button onClick={() => subscription()}>구도구도구독</button>;
}
export default App;
