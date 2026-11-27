import { mostrarAfn } from './visualizacoes.js';

$('afn-simular').onclick = () => executarAutomato('afn', '/api/afn', mostrarAfn);
$('afn-exemplo').onclick = () => carregarExemplo('afn', 'afn-termina-em-ab.json');
$('afn-exemplo2').onclick = () => carregarExemplo('afn', 'afn-lambda-a-ou-b.json');
$('afn-cadeia').addEventListener('keydown', e => { if (e.key === 'Enter') $('afn-simular').click(); });
ligarArquivoJson('afn');
carregarExemplo('afn', 'afn-termina-em-ab.json');
