export default function getConfigProd(): { url: string, realm: string, clientId: string } {
  return {
    url: 'http://localhost:8080',
    realm: 'VOTING',
    clientId: 'VOTING_FRONT_END'
  }
};
