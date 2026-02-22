import React, { useState, useRef, useEffect } from 'react';
import Stomp, { Client } from 'stompjs';
import SockJS from 'sockjs-client';
import { v4 as uuidv4 } from 'uuid';
import { uploadFile } from '../api/fileApi';

export const FileUpload = () => {
    const [file, setFile] = useState<File | null>(null);
    const [progress, setProgress] = useState<number>(0);
    const [connected, setConnected] = useState<boolean>(false);

    const stompClient = useRef<Client | null>(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws-excel');
        const client = Stomp.over(socket);

        client.connect({}, () => {
            console.log("WebSocket connected");
            stompClient.current = client;
            setConnected(true);
        });

        return () => {
            if (stompClient.current) {
                stompClient.current.disconnect(() => {
                    console.log("Disconnected");
                });
            }
        };
    }, []);

    const handleUpload = async () => {
        if (!file) return;

        if (!stompClient.current || !connected) {
            console.error("WebSocket not connected yet!");
            return;
        }

        setProgress(0);

        // 1️⃣ Generate taskId locally
        const taskId = uuidv4();

        // 2️⃣ Subscribe BEFORE uploading
        const subscription = stompClient.current.subscribe(
            `/topic/progress/${taskId}`,
            (message) => {
                const val = parseInt(message.body);
                setProgress(val);

                // auto unsubscribe when complete
                if (val >= 100) {
                    subscription.unsubscribe();
                }
            }
        );

        // 3️⃣ Upload file with taskId
        try {
            await uploadFile(file, taskId);
        } catch (error) {
            console.error("Upload failed", error);
        }
    };

    return (
        <div className="max-w-xl mx-auto mt-16 p-8 bg-white rounded-2xl shadow-xl border border-gray-200">

            {/* File Input */}
            <label className="block text-sm font-semibold text-gray-700 mb-2">
                Select Excel File
            </label>

            <input
                type="file"
                onChange={(e) => setFile(e.target.files?.[0] || null)}
                className="block w-full text-sm text-gray-600
               file:mr-4 file:py-2 file:px-4
               file:rounded-lg file:border-0
               file:text-sm file:font-semibold
               file:bg-blue-50 file:text-blue-700
               hover:file:bg-blue-100
               cursor-pointer"
            />

            {/* Upload Button */}
            <button
                onClick={handleUpload}
                disabled={!file || !connected}
                className={`mt-6 w-full py-3 rounded-xl text-white font-medium transition-all
      ${!file || !connected
                        ? "bg-gray-400 cursor-not-allowed"
                        : "bg-blue-600 hover:bg-blue-700 active:scale-95"
                    }`}
            >
                {connected ? "Upload & Track" : "Connecting..."}
            </button>

            {/* Progress Bar */}
            <div className="mt-6">
                <div className="w-full bg-gray-200 rounded-full h-5 overflow-hidden">
                    <div
                        className="bg-green-500 h-5 rounded-full transition-all duration-300"
                        style={{ width: `${progress}%` }}
                    />
                </div>

                <p className="text-sm text-gray-600 mt-2 text-center">
                    {progress}% complete
                </p>
            </div>

        </div>
    );
};