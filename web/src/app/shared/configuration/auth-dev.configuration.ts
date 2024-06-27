export default function getConfigDev(): { url: string, realm: string, clientId: string } {
  return {
    url: 'http://localhost:8080',
    realm: 'VOTING',
    clientId: 'VOTING_FRONT_END'
  }
};
