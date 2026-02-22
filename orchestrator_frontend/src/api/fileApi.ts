export const uploadFile = async (file: File, taskId: string): Promise<void> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('taskId', taskId);
    const response = await fetch('http://localhost:8080/api/excel/upload', {
        method: 'POST',
        body: formData,
    });

    if (!response.ok) {
        throw new Error('Upload failed');
    }
};