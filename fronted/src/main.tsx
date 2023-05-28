import React from 'react'
import ReactDOM from 'react-dom/client'
import LikeButton from './App.tsx'
import './index.css'
import Btn from './btn.tsx'
import Test from './test.tsx'


ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <LikeButton />
    <Btn />
    <LikeButton />
    <Test />
  </React.StrictMode>,
)
