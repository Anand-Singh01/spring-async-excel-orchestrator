import React, { useEffect } from 'react'
import { establishSocketConnection } from '../api/socket';
import { FileUpload } from '../components/FileUpload';

const Uploadpage = () => {
  useEffect(() => {
    establishSocketConnection();
  }, [])
  return (
    <div>
      <FileUpload />
    </div>
  )
}

export default Uploadpage