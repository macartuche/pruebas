import { IPais, NewPais } from './pais.model';

export const sampleWithRequiredData: IPais = {
  id: 22424,
};

export const sampleWithPartialData: IPais = {
  id: 8945,
  nombre: 'solidly tugboat',
};

export const sampleWithFullData: IPais = {
  id: 5820,
  nombre: 'which off',
};

export const sampleWithNewData: NewPais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
