// 编辑器配置
export const editorConfig = {
  placeholder: '请输入课程内容...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/upload/image',
      fieldName: 'image',
      maxFileSize: 5 * 1024 * 1024, // 5MB
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      metaWithUrl: true,
      withCredentials: true,
      timeout: 5 * 1000,
      onBeforeUpload(file) {
        return file
      },
      onSuccess(file, res) {
        console.log('Image uploaded:', res)
        return res.data.url
      },
      onFailed(file, res) {
        console.error('Upload failed:', res)
        ElMessage.error('图片上传失败')
      },
      onError(file, err) {
        console.error('Upload error:', err)
        ElMessage.error('图片上传出错')
      }
    }
  }
}

// 工具栏配置
export const toolbarConfig = {
  excludeKeys: [
    'group-video',
    'group-code',
    'group-todo'
  ]
}

// 编辑器模式
export const EDITOR_MODE = {
  DEFAULT: 'default',
  SIMPLE: 'simple'
} 