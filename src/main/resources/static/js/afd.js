import { mostrarAfd } from './visualizacoes.js';

$('afd-simular').onclick = () => executarAutomato('afd', '/api/afd', mostrarAfd);
$('afd-exemplo').onclick = () => carregarExemplo('afd', 'afd-enunciado.json');
$('afd-exemplo2').onclick = () => carregarExemplo('afd', 'afd-numero-par-de-as.json');
$('afd-cadeia').addEventListener('keydown', e => { if (e.key === 'Enter') $('afd-simular').click(); });
ligarArquivoJson('afd');
carregarExemplo('afd', 'afd-enunciado.json');
