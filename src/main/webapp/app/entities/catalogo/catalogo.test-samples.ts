import { ICatalogo, NewCatalogo } from './catalogo.model';

export const sampleWithRequiredData: ICatalogo = {
  id: 685,
  codigo: 'yet hmph',
};

export const sampleWithPartialData: ICatalogo = {
  id: 32341,
  codigo: 'beside tidy epauliere',
  descripcion: 'chubby sans fully',
  nuevocampo: 'frantically potentially ultimatum',
  observacion: 'ultimately drat seldom',
};

export const sampleWithFullData: ICatalogo = {
  id: 26726,
  codigo: 'supposing however tog',
  descripcion: 'phooey interleave',
  nuevocampo: 'gleeful minty below',
  valor: 9656.42,
  activo: false,
  observacion: 'outside',
};

export const sampleWithNewData: NewCatalogo = {
  codigo: 'supposing accurate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
