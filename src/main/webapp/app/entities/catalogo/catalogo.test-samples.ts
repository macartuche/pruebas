import { ICatalogo, NewCatalogo } from './catalogo.model';

export const sampleWithRequiredData: ICatalogo = {
  id: 13523,
  codigo: 'over',
};

export const sampleWithPartialData: ICatalogo = {
  id: 21417,
  codigo: 'geez',
  nuevocampo: 'phooey well-lit excluding',
};

export const sampleWithFullData: ICatalogo = {
  id: 10178,
  codigo: 'chubby sans fully',
  descripcion: 'frantically potentially ultimatum',
  nuevocampo: 'ultimately drat seldom',
  valor: 26725.94,
  activo: false,
};

export const sampleWithNewData: NewCatalogo = {
  codigo: 'aw vacantly luggage',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
