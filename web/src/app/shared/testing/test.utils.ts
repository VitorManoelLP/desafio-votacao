import { Type } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";

export class TestUtils {

  public static configure(module: any, component: Type<unknown>, options: any = {}): Promise<ComponentFixture<any>> {
    TestBed.configureTestingModule({
    imports: [module],
    declarations: options.declarations,
    providers: options.providers,
    teardown: { destroyAfterEach: false }
  });
    return TestBed.compileComponents().then(() => {
      let fixture = TestBed.createComponent(component);
      fixture.detectChanges();
      return fixture;
    });

  }
}
