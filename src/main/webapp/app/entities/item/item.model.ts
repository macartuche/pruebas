import { ICatalogo } from 'app/entities/catalogo/catalogo.model';
import { IPais } from 'app/entities/pais/pais.model';

export interface IItem {
  id: number;
  codigo?: string | null;
  codigoCatalogo?: string | null;
  descripcion?: string | null;
  catalogo?: Pick<ICatalogo, 'id'> | null;
  pais?: Pick<IPais, 'id'> | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
