import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICatalogo, NewCatalogo } from '../catalogo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogo for edit and NewCatalogoFormGroupInput for create.
 */
type CatalogoFormGroupInput = ICatalogo | PartialWithRequiredKeyOf<NewCatalogo>;

type CatalogoFormDefaults = Pick<NewCatalogo, 'id' | 'activo'>;

type CatalogoFormGroupContent = {
  id: FormControl<ICatalogo['id'] | NewCatalogo['id']>;
  codigo: FormControl<ICatalogo['codigo']>;
  descripcion: FormControl<ICatalogo['descripcion']>;
  nuevocampo: FormControl<ICatalogo['nuevocampo']>;
  valor: FormControl<ICatalogo['valor']>;
  activo: FormControl<ICatalogo['activo']>;
  observacion: FormControl<ICatalogo['observacion']>;
};

export type CatalogoFormGroup = FormGroup<CatalogoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CatalogoFormService {
  createCatalogoFormGroup(catalogo: CatalogoFormGroupInput = { id: null }): CatalogoFormGroup {
    const catalogoRawValue = {
      ...this.getFormDefaults(),
      ...catalogo,
    };
    return new FormGroup<CatalogoFormGroupContent>({
      id: new FormControl(
        { value: catalogoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(catalogoRawValue.codigo, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(catalogoRawValue.descripcion),
      nuevocampo: new FormControl(catalogoRawValue.nuevocampo),
      valor: new FormControl(catalogoRawValue.valor),
      activo: new FormControl(catalogoRawValue.activo),
      observacion: new FormControl(catalogoRawValue.observacion),
    });
  }

  getCatalogo(form: CatalogoFormGroup): ICatalogo | NewCatalogo {
    return form.getRawValue() as ICatalogo | NewCatalogo;
  }

  resetForm(form: CatalogoFormGroup, catalogo: CatalogoFormGroupInput): void {
    const catalogoRawValue = { ...this.getFormDefaults(), ...catalogo };
    form.reset(
      {
        ...catalogoRawValue,
        id: { value: catalogoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CatalogoFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
