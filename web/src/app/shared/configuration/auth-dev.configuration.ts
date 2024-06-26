export default function getConfigDev(): { url: string, realm: string, clientId: string } {
  return {
    url: 'http://localhost:8080',
    realm: 'SOCIAL_MEDIA',
    clientId: 'social_media_frontend'
  }
};
